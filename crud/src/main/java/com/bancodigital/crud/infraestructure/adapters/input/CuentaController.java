package com.bancodigital.crud.infraestructure.adapters.input;


import com.bancodigital.crud.application.ports.input.CuentaService;
import com.bancodigital.crud.domain.model.Cuenta;
import com.bancodigital.crud.infraestructure.adapters.output.CuentaRequest;
import com.bancodigital.crud.infraestructure.adapters.output.CuentaMapper;
import com.bancodigital.crud.infraestructure.adapters.output.CuentaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class CuentaController {

    private final CuentaService cuentaService;
    private final CuentaMapper cuentaMapper;

    @PostMapping
    public ResponseEntity<CuentaResponse> createCuenta(@RequestBody CuentaRequest request){
        try{

            Cuenta newCuenta = this.cuentaMapper.toDomain(request);
            Cuenta createCuenta = this.cuentaService.createCuenta(newCuenta);

            CuentaResponse response = cuentaMapper.toResponse(createCuenta);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error inesperado al grabar cuenta", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CuentaResponse>> getAllCuentas(){
        try{
            log.info("Buscando todas las cuentas");
            List<Cuenta> cuentas = this.cuentaService.findAllCuentas();
            List<CuentaResponse> responses = this.cuentaMapper.toResponse(cuentas);

            log.info("Encontro {} clientes", responses.size());
            return ResponseEntity.ok(responses);

        }catch (Exception e) {
            log.error("Error inesperado al buscar clientes", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable String id) {
        try {
            log.info("Borrando cuenta con ID: {}", id);

            cuentaService.deleteCuenta(id);

            log.info("cuenta borrada exitosamente con ID: {}", id);
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            log.error("Error inesperado al borrar cuenta", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    //no se hizo update
}
