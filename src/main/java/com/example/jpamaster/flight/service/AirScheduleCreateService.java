package com.example.jpamaster.flight.service;

import com.example.jpamaster.flight.domain.entity.AirSchedule;
import com.example.jpamaster.flight.domain.entity.AirScheduleSeatType;
import com.example.jpamaster.flight.domain.entity.Airplane;
import com.example.jpamaster.flight.domain.entity.Airport;
import com.example.jpamaster.flight.domain.repository.AirScheduleRepository;
import com.example.jpamaster.flight.exception.NotFoundException;
import com.example.jpamaster.flight.web.dto.req.AirScheduleRequestDto;
import com.example.jpamaster.flight.web.dto.res.AirScheduleResponseDto;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AirScheduleCreateService {

    private final SeatService seatService;
    private final FlightValidationService flightValidationService;
    private final AirScheduleRepository airScheduleRepository;

    @Transactional
    public AirScheduleResponseDto createAirSchedule(AirScheduleRequestDto dto) {

        // TODO 비행 스케줄 검증 후 비행 스케줄 등록 필요 - 타이트한 검증 처리
        Airport fromAirport = flightValidationService.airScheduleAirportValidation(dto.getFromAirportSeq());
        Airport toAirport = flightValidationService.airScheduleAirportValidation(dto.getToAirportSeq());
        Airplane airplane = flightValidationService.airScheduleAirplaneValidation(dto.getAirplaneSeq());
        flightValidationService.airplaneSeatValidation(airplane.getAirplaneSeatTypes(),
            dto.getAirScheduleSeatRequestDtos());
        flightValidationService.availableAirlineValidation(airplane, fromAirport, toAirport);
        flightValidationService.takeOffTimeValidation(fromAirport.getLocationEn(), dto.getExpectedTakeoffDate(),
            dto.getExpectedTakeoffTime());

        // 스케줄 등록
        AirSchedule airSchedule = AirSchedule.createAirSchedule(fromAirport, toAirport, airplane);
        airSchedule.calculateAirSchedule(dto.getExpectedTakeoffDate(), dto.getExpectedTakeoffTime());

        // 좌석 정보 등록
        Set<AirScheduleSeatType> airScheduleSeatTypes = seatService.registerSeatForAirSchedule(
            dto.getAirScheduleSeatRequestDtos());
        airScheduleSeatTypes.forEach(airScheduleSeatType -> airScheduleSeatType.registerAirSchedule(airSchedule));

        // 저장
        AirSchedule savedAirSchedule = airScheduleRepository.save(airSchedule);

        return new AirScheduleResponseDto(
            savedAirSchedule.getAirScheduleSeq(),
            savedAirSchedule.getDepartAt(),
            savedAirSchedule.getArriveAt(),
            savedAirSchedule.getFlightDistanceKm(),
            savedAirSchedule.getEstimatedHourSpent(),
            savedAirSchedule.getEstimatedMinuteSpent()
        );
    }


    @Transactional
    public AirScheduleResponseDto updateAirSchedule(Long airScheduleSeq, AirScheduleRequestDto dto) {
        AirSchedule airSchedule = airScheduleRepository.findById(airScheduleSeq)
            .orElseThrow(() -> new NotFoundException("해당 스케줄 정보가 존재하지 않습니다."));

        Airport fromAirport = flightValidationService.airScheduleAirportValidation(dto.getFromAirportSeq());
        Airport toAirport = flightValidationService.airScheduleAirportValidation(dto.getToAirportSeq());
        Airplane airplane = airSchedule.getAirplane();
        flightValidationService.airplaneSeatValidation(airplane.getAirplaneSeatTypes(),
            dto.getAirScheduleSeatRequestDtos());
        flightValidationService.availableAirlineValidation(airplane, fromAirport, toAirport);
        flightValidationService.takeOffTimeValidation(fromAirport.getLocationEn(), dto.getExpectedTakeoffDate(),
            dto.getExpectedTakeoffTime());

        airSchedule.updateAirSchedule(fromAirport, toAirport, dto.getExpectedTakeoffDate(),
            dto.getExpectedTakeoffTime());

        return new AirScheduleResponseDto(
            airSchedule.getAirScheduleSeq(),
            airSchedule.getDepartAt(),
            airSchedule.getArriveAt(),
            airSchedule.getFlightDistanceKm(),
            airSchedule.getEstimatedHourSpent(),
            airSchedule.getEstimatedMinuteSpent()
        );
    }
}
