package com.bancodigital.crud.application.usecases;

import com.bancodigital.crud.application.ports.input.ClienteService;
import com.bancodigital.crud.application.ports.output.ClienteRepositoryPort;
import com.bancodigital.crud.domain.model.Cliente;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepositoryPort clienteRepositoryPort;

    @Override
    @Transactional
    public Cliente createCliente(Cliente newCliente) {
        if(newCliente == null){
            throw new RuntimeException("Cliente no puede ser nulo");
        }

        //ejecutar validaciones del cliente:
        //Email formato valido
        //Documento unico en el sistema
        validacionesRegistroCliente(newCliente);

        return clienteRepositoryPort.save(newCliente);
    }

    private void validacionesRegistroCliente(Cliente newCliente) {

        if(!newCliente.hasValidEmail()){
            throw new RuntimeException("Cliente no tiene correo valido");
        }
        if(!newCliente.hasValidDocumento()){
            throw  new RuntimeException("Cliente no tiene documento valido");
        }else{
            if(clienteRepositoryPort.existeByDocumento(newCliente.getDocumento())){
                throw new RuntimeException("Documento ya existe en bd");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAllClientes() {

        return clienteRepositoryPort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findCliente(String clienteId) {
        if (clienteId == null || clienteId.trim().isEmpty()) {
            throw new RuntimeException("Id de cliente invalido");
        }
        return clienteRepositoryPort.findById(clienteId);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findClienteByName(String nombre) {
        if(nombre ==null || nombre.trim().isEmpty()){
            throw new RuntimeException("Nombre a buscar no puede ser invalido");
        }

        return clienteRepositoryPort.findByNameContaining(nombre.trim());
    }

    @Override
    @Transactional
    public Cliente updateCliente(String id, Cliente cliente) {
        if (id == null || id.trim().isEmpty()) {
            throw new RuntimeException("Id invalido");
        }

        Cliente clienteExistente =  clienteRepositoryPort.findById(id);

        if(clienteExistente == null){
            throw new RuntimeException("Cliente no encontrado");
        }

        //validaciones de cliente
        validacionesModificacionCliente(cliente,clienteExistente);

        //actualizamos el cliente
        clienteExistente.updateCliente(cliente.getNombre(), cliente.getEmail(), cliente.getDocumento());
        return clienteRepositoryPort.save(clienteExistente);
    }


    @Override
    public void deleteCliente(String clienteId) {
        if (clienteId == null || clienteId.trim().isEmpty()) {
            throw new RuntimeException("Id de cliente invalido");
        }

        if(clienteRepositoryPort.findById(clienteId)==null){
            throw new RuntimeException("Cliente no existe");
        }

        clienteRepositoryPort.deleteCliente(clienteId);
    }


    private void validacionesModificacionCliente(Cliente cliente, Cliente clienteExistente) {

        if(!cliente.hasValidNombre()){
            throw new RuntimeException("Nombre debe tener al menos 2 caracteres");
        }
        if(!cliente.hasValidEmail()){
            throw new RuntimeException("email de formato invalido");
        }

        //coteja si existe cambio de email o si el email ya existe
        if(!clienteExistente.getEmail().equals(cliente.getEmail()) &&
                clienteRepositoryPort.existeByEmail(cliente.getEmail())){
            throw  new RuntimeException("Email ya existia"+cliente.getEmail());
        }

        if(!clienteExistente.getDocumento().equals(cliente.getDocumento()) &&
                clienteRepositoryPort.existeByDocumento(cliente.getDocumento())){
            throw  new RuntimeException("Documento ya existia"+cliente.getDocumento());
        }

    }
}
