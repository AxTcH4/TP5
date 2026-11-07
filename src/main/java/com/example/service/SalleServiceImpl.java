package com.example.service;

import com.example.model.Salle;
import com.example.repository.SalleRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class SalleServiceImpl implements SalleService {
    private EntityManager em;

    @Inject
    private SalleRepository salleRepository;

    public SalleServiceImpl(SalleRepository salleRepository) {
        this.salleRepository = salleRepository;
    }


    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
    @Override
    public List<Salle> getAvailableRooms(LocalDateTime debut, LocalDateTime fin) {
        // Ici on pourrait ajouter des règles métier avant d'appeler le repository
        return salleRepository.findAvailableRooms(debut, fin);
    }

    @Override
    public List<Salle> searchRooms(String nom, Integer capaciteMin, LocalDateTime debut, LocalDateTime fin, int page, int size) {
        return salleRepository.searchRooms(nom, capaciteMin, debut, fin, page, size);
    }

    @Override
    public long countRooms(String nom, Integer capaciteMin, LocalDateTime debut, LocalDateTime fin) {
        return salleRepository.countRooms(nom, capaciteMin, debut, fin);
    }
}