package com.example.SAPA.Repositories;

import com.example.SAPA.DTOs.Response.UserDTOResponse;
import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.Models.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
        // Query que busca una ubicación mediante el Place ID de Google.
        // Se utiliza durante el registro de usuarios para verificar si la ubicación
        // ya existe en la base de datos y evitar crear registros duplicados.
    Optional<LocationEntity> findByGooglePlaceId(String googlePlaceId);

        // Query que busca usuarios dentro de un radio determinado tomando como referencia
        // la laitud y longitud centrales. Retorna todos los usuarios ubicados a menos de
        // 20km del punto indicado y se utiliza para filtrar pacientes, médicos, ubicaciones o foros.
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
        List<UserEntity> findUsersWithinRadius(
                @Param("latCentro") Double latCentro,
                @Param("lngCentro") Double lngCentro,
                @Param("radioKm") Double radioKm
        );

}
