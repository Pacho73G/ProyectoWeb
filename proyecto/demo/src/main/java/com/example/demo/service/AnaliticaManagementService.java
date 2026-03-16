package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.CheckpointRecorrido;
import com.example.demo.model.MapaCalor;
import com.example.demo.model.MetricaDocente;
import com.example.demo.model.Reconocimiento;
import com.example.demo.model.Recorrido;
import com.example.demo.repository.CheckpointRecorridoRepository;
import com.example.demo.repository.MapaCalorRepository;
import com.example.demo.repository.MetricaDocenteRepository;
import com.example.demo.repository.ReconocimientoRepository;
import com.example.demo.repository.RecorridoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnaliticaManagementService {

    private final RecorridoRepository recorridoRepository;
    private final CheckpointRecorridoRepository checkpointRecorridoRepository;
    private final MapaCalorRepository mapaCalorRepository;
    private final MetricaDocenteRepository metricaDocenteRepository;
    private final ReconocimientoRepository reconocimientoRepository;

    public Recorrido guardar(Recorrido entity) { return recorridoRepository.save(entity); }
    public void eliminarRecorrido(Long id) { recorridoRepository.deleteById(id); }
    public CheckpointRecorrido guardar(CheckpointRecorrido entity) { return checkpointRecorridoRepository.save(entity); }
    public void eliminarCheckpoint(Long id) { checkpointRecorridoRepository.deleteById(id); }
    public MapaCalor guardar(MapaCalor entity) { return mapaCalorRepository.save(entity); }
    public void eliminarMapaCalor(Long id) { mapaCalorRepository.deleteById(id); }
    public MetricaDocente guardar(MetricaDocente entity) { return metricaDocenteRepository.save(entity); }
    public void eliminarMetrica(Long id) { metricaDocenteRepository.deleteById(id); }
    public Reconocimiento guardar(Reconocimiento entity) { return reconocimientoRepository.save(entity); }
    public void eliminarReconocimiento(Long id) { reconocimientoRepository.deleteById(id); }
}
