package com.bancodigital.crud.infraestructure.adapters.input;


import com.bancodigital.crud.application.ports.input.TransaccionService;
import com.bancodigital.crud.domain.model.Transaccion;
import com.bancodigital.crud.infraestructure.adapters.output.TransaccionMapper;
import com.bancodigital.crud.infraestructure.adapters.output.TransaccionRequest;
import com.bancodigital.crud.infraestructure.adapters.output.TransaccionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class TransaccionController {

    private final TransaccionService transaccionService;
    private final TransaccionMapper trxMapper;


    @PostMapping
    public ResponseEntity<TransaccionResponse> realizarTransferencia(@RequestBody TransaccionRequest request){
        try{
            Transaccion newTrx = this.trxMapper.toDomain(request);
            Transaccion createTrx = this.transaccionService.realizarTransferencia(newTrx);

            TransaccionResponse response = trxMapper.toResponse(createTrx);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        }catch (Exception e) {
            log.error("Error inesperado al realizar transferencia", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<TransaccionResponse>> getAllTrx(){
        try{
            log.info("Buscando todas las Trx");
            List<Transaccion> trxs = this.transaccionService.findAllTransacciones();
            List<TransaccionResponse> responses = this.trxMapper.toResponse(trxs);

            log.info("Encontro {} trx", responses.size());
            return ResponseEntity.ok(responses);

        }catch (Exception e) {
            log.error("Error inesperado al buscar trx", e);
            return ResponseEntity.internalServerError().build();
        }

    }
}
