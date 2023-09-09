package com.cons.reporteya.service;

import com.cons.reporteya.entity.Marker;
import com.cons.reporteya.repository.MarkerRepository;
import org.springframework.stereotype.Service;

@Service
public class MarkerService {

    private final MarkerRepository markerRepository;

    public MarkerService(MarkerRepository markerRepository) {
        this.markerRepository = markerRepository;
    }

    public Marker save(Marker marker) {
        return markerRepository.save(marker);
    }
}
