package com.example.SAPA.Repositories;

import com.example.SAPA.DTOs.Response.UserDTOResponse;
import com.example.SAPA.Models.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    @Query(value = """
    SELECT u.*
    FROM users u
    INNER JOIN locations l ON u.location_id = l.id
    WHERE (
        6371 * acos(
            cos(radians(:latCentro)) *
            cos(radians(l.latitude)) *
            cos(radians(l.longitude) - radians(:lngCentro)) +
            sin(radians(:latCentro)) *
            sin(radians(l.latitude))
        )
    ) <= :radioKm
    """, nativeQuery = true)
    List<UserDTOResponse> findUsersWithinRadius(
            @Param("latCentro") Double latCentro,
            @Param("lngCentro") Double lngCentro,
            @Param("radioKm") Double radioKm
    );

}
