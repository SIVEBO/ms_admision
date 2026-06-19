package com.sivebo.ms_admision.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.sivebo.ms_admision.exception.EntityNotFoundException;
import com.sivebo.ms_admision.exception.ExternalServiceException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebClientUtil {

        /**
         * Valida que exista un recurso en otro microservicio consultando GET {path}.
         * Lanza EntityNotFoundException si el remoto responde 404,
         * o ExternalServiceException si hay un problema de conexion.
         */
        public void validateExists(Long id, String nombreRecurso, String path, WebClient webClient) {
                try {
                        webClient.get()
                                        .uri(path, id)
                                        .retrieve()
                                        .bodyToMono(String.class)
                                        .block();
                        log.info(">>> {} con id {} validado correctamente (WebClient)", nombreRecurso, id);
                } catch (WebClientResponseException.NotFound notFound) {
                        throw new EntityNotFoundException(
                                        nombreRecurso + " con id " + id + " no existe en el microservicio correspondiente");
                } catch (Exception exception) {
                        throw new ExternalServiceException(
                                        "No se pudo validar " + nombreRecurso + ": " + exception.getMessage());
                }
        }

        /**
         * Valida que exista un recurso en otro microservicio consultando GET {path}?{paramName}={id}.
         * Util para servicios cuyo endpoint de busqueda usa query params en vez de path variable
         * (ej. ms_sucursales: /api/v1/sucursales/buscar?id=X).
         */
        public void validateExistsByQueryParam(Long id, String nombreRecurso, String path, String paramName,
                        WebClient webClient) {
                try {
                        webClient.get()
                                        .uri(uriBuilder -> uriBuilder
                                                        .path(path)
                                                        .queryParam(paramName, id)
                                                        .build())
                                        .retrieve()
                                        .bodyToMono(String.class)
                                        .block();
                        log.info(">>> {} con id {} validado correctamente (WebClient)", nombreRecurso, id);
                } catch (WebClientResponseException.NotFound notFound) {
                        throw new EntityNotFoundException(
                                        nombreRecurso + " con id " + id + " no existe en el microservicio correspondiente");
                } catch (Exception exception) {
                        throw new ExternalServiceException(
                                        "No se pudo validar " + nombreRecurso + ": " + exception.getMessage());
                }
        }

        /**
         * Dispara la creacion de un recurso en otro microservicio via POST {path}.
         * Devuelve el cuerpo de la respuesta como String (no bloquea el flujo si el
         * servicio remoto responde, pero propaga error si esta caido).
         */
        public <T> String postCrear(String path, T body, String nombreRecurso, WebClient webClient) {
                try {
                        String respuesta = webClient.post()
                                        .uri(path)
                                        .bodyValue(body)
                                        .retrieve()
                                        .bodyToMono(String.class)
                                        .block();
                        log.info(">>> {} creado correctamente via WebClient (POST {})", nombreRecurso, path);
                        return respuesta;
                } catch (Exception exception) {
                        throw new ExternalServiceException(
                                        "No se pudo crear " + nombreRecurso + ": " + exception.getMessage());
                }
        }
}
