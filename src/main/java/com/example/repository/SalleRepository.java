package com.example.repository;

import com.example.model.Salle;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

public class SalleRepository {
    private EntityManager entityManager;
    public SalleRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // 1. Find available rooms
    public List<Salle> findAvailableRooms(
            LocalDateTime debutRecherche,
            LocalDateTime finRecherche) {

        String jpql =
                "SELECT s FROM Salle s WHERE NOT EXISTS (" +
                        "SELECT r FROM Reservation r WHERE r.salle = s " +
                        "AND (:debut < r.fin AND :fin > r.debut)" +
                        ")";

        TypedQuery<Salle> query =
                entityManager.createQuery(jpql, Salle.class);

        query.setParameter("debut", debutRecherche);
        query.setParameter("fin", finRecherche);

        return query.getResultList();
    }

    // 2. Search rooms with filters and pagination
    public List<Salle> searchRooms(
            String nom,
            Integer capaciteMin,
            LocalDateTime debut,
            LocalDateTime fin,
            int page,
            int size) {

        StringBuilder jpql = new StringBuilder(
                "SELECT s FROM Salle s WHERE 1=1 "
        );

        if (nom != null && !nom.trim().isEmpty()) {
            jpql.append("AND LOWER(s.nom) LIKE LOWER(:nom) ");
        }

        if (capaciteMin != null) {
            jpql.append("AND s.capacite >= :capaciteMin ");
        }

        if (debut != null && fin != null) {
            jpql.append(
                    "AND NOT EXISTS (" +
                            "SELECT r FROM Reservation r WHERE r.salle = s " +
                            "AND (:debut < r.fin AND :fin > r.debut)" +
                            ") "
            );
        }

        TypedQuery<Salle> query =
                entityManager.createQuery(jpql.toString(), Salle.class);

        if (nom != null && !nom.trim().isEmpty()) {
            query.setParameter("nom", "%" + nom.trim() + "%");
        }

        if (capaciteMin != null) {
            query.setParameter("capaciteMin", capaciteMin);
        }

        if (debut != null && fin != null) {
            query.setParameter("debut", debut);
            query.setParameter("fin", fin);
        }

        // Pagination
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    // 3. Count rooms matching the filters
    public long countRooms(
            String nom,
            Integer capaciteMin,
            LocalDateTime debut,
            LocalDateTime fin) {

        StringBuilder jpql = new StringBuilder(
                "SELECT COUNT(s) FROM Salle s WHERE 1=1 "
        );

        if (nom != null && !nom.trim().isEmpty()) {
            jpql.append("AND LOWER(s.nom) LIKE LOWER(:nom) ");
        }

        if (capaciteMin != null) {
            jpql.append("AND s.capacite >= :capaciteMin ");
        }

        if (debut != null && fin != null) {
            jpql.append(
                    "AND NOT EXISTS (" +
                            "SELECT r FROM Reservation r WHERE r.salle = s " +
                            "AND (:debut < r.fin AND :fin > r.debut)" +
                            ") "
            );
        }

        TypedQuery<Long> query =
                entityManager.createQuery(jpql.toString(), Long.class);

        if (nom != null && !nom.trim().isEmpty()) {
            query.setParameter("nom", "%" + nom.trim() + "%");
        }

        if (capaciteMin != null) {
            query.setParameter("capaciteMin", capaciteMin);
        }

        if (debut != null && fin != null) {
            query.setParameter("debut", debut);
            query.setParameter("fin", fin);
        }

        return query.getSingleResult();
    }

}
