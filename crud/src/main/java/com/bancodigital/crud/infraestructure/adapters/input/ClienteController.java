package com.bancodigital.crud.infraestructure.adapters.input;

import com.bancodigital.crud.application.ports.input.ClienteService;
import com.bancodigital.crud.domain.model.Cliente;
import com.bancodigital.crud.infraestructure.adapters.output.ClienteMapper;
import com.bancodigital.crud.infraestructure.adapters.output.ClienteRequest;
import com.bancodigital.crud.infraestructure.adapters.output.ClienteResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    @PostMapping
    public ResponseEntity<ClienteResponse> createCliente(@Valid @RequestBody ClienteRequest request){
        try{

            Cliente newCliente = this.clienteMapper.toDomain(request);
            Cliente createCliente = this.clienteService.createCliente(newCliente);

            ClienteResponse response = clienteMapper.toResponse(createCliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }  catch (Exception e) {
        log.error("Error inesperado al grabar cliente", e);
        return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> getCliente(@PathVariable String id) {
        try {
            log.info("Buscando cliente con ID: {}", id);

            Cliente cliente = this.clienteService.findCliente(id);
            log.info("cliente encontrado: {}", cliente.getClienteId());

            return ResponseEntity.ok(this.clienteMapper.toResponse(cliente));

        } catch (Exception e) {
            log.warn("Cliente no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> getAllClientes() {
        try {
            log.info("Buscando todos los clientes");

            List<Cliente> clientes = this.clienteService.findAllClientes();
            List<ClienteResponse> responses = this.clienteMapper.toResponse(clientes);

            log.info("Encontro {} clientes", responses.size());
            return ResponseEntity.ok(responses);

        } catch (Exception e) {
            log.error("Error inesperado al buscar clientes", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ClienteResponse>> searchClientesByNombre(@RequestParam String nombre) {
        try {
            log.info("Buscando clientes por nombre {}", nombre);

            List<Cliente> clientes = this.clienteService.findClienteByName(nombre);
            List<ClienteResponse> responses = this.clienteMapper.toResponse(clientes);

            log.info("Encontro {} clientes con el nombre '{}'", responses.size(), nombre);
            return ResponseEntity.ok(responses);

         } catch (Exception e) {
            log.error("Error inesperado al buscar cliente", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    //falta update

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable String id) {
        try {
            log.info("Borrando cliente con ID: {}", id);

            clienteService.deleteCliente(id);

            log.info("cliente borrado exitosamente con ID: {}", id);
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            log.error("Error inesperado al borrar cliente", e);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> updateCliente(@PathVariable String id, @Valid @RequestBody ClienteRequest request) {
        try {
            log.info("Actualizando cliente with ID: {} with data: nombre={}, email={}", id, request.getNombre(), request.getEmail());

            Cliente cliente = clienteMapper.toDomain(request);
            Cliente clienteActualizado = clienteService.updateCliente(id, cliente);

            if (clienteActualizado == null) {
                log.warn("service del cliente devuelve null para el update");
                return ResponseEntity.badRequest().build();
            }

            log.info("Cliente actualizado exitosamente con ID: {}", clienteActualizado.getClienteId());
            return ResponseEntity.ok(clienteMapper.toResponse(clienteActualizado));

        } catch (Exception e) {
            log.error("Error inesperado al actualizar cliente", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
