package com.bancodigital.crud.application.usecases;

import com.bancodigital.crud.application.ports.input.CuentaService;
import com.bancodigital.crud.application.ports.output.ClienteRepositoryPort;
import com.bancodigital.crud.application.ports.output.CuentaRepositoryPort;
import com.bancodigital.crud.domain.model.Cliente;
import com.bancodigital.crud.domain.model.Cuenta;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Slf4j
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepositoryPort cuentaRepositoryPort;
    private final ClienteRepositoryPort clienteRepositoryPort;

    @Override
    public Cuenta createCuenta(Cuenta newCuenta) {
        if(newCuenta == null || newCuenta.getCliente()==null || newCuenta.getCliente().getClienteId()==null ){
            throw new RuntimeException("Cuenta o cliente no puede ser nulo");
        }

        //validaciones de negocio:
        validacionesRegistroCuenta(newCuenta);
        //verificamos si ya existia el cliente:
        Cliente clienteExistente =  clienteRepositoryPort.findById(newCuenta.getCliente().getClienteId());

        Cuenta cuenta = new Cuenta();
        cuenta.setCuentaId(newCuenta.getCuentaId());
        cuenta.setNumCuenta(newCuenta.getNumCuenta());
        cuenta.setEstado(newCuenta.getEstado());
        cuenta.setSaldo(newCuenta.getSaldo());

        if(clienteExistente == null){
            //grabamos el cliente es nuevo:
            Cliente newCliente = newCuenta.getCliente();
            validarClienteNuevo(newCliente);
            Cliente clienteGuardado = clienteRepositoryPort.save(newCliente);
            log.info("grabo el nuevo cliente "+clienteGuardado.getDocumento()+", ahora grabara la cuenta");
            cuenta.setCliente(clienteGuardado);
        }else{
            log.info("cliente ya existia "+clienteExistente.getDocumento());
            //creamos la cuenta para el cliente existente:
            cuenta.setCliente(clienteExistente);

        }
        log.info("se grabara la cuenta");
        return cuentaRepositoryPort.save(cuenta);

    }

    private void validarClienteNuevo(Cliente newCliente) {

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

    //validaciones de negocio:
    //saldo >= 0
    //numero_cuenta único
    //estado valores permitidos: 'ACTIVO', 'CERRADO'
    private void validacionesRegistroCuenta(Cuenta newCuenta) {
        if(!newCuenta.hasValidSaldo()){
            throw new RuntimeException("El saldo no puede ser negativo ni nulo");
        }

        if(!newCuenta.hasValidNumCuenta()){
            throw new RuntimeException("El número de cuenta es inválido");
        }

        if(!cuentaRepositoryPort.esCuentaUnica(newCuenta)){
            throw new RuntimeException("Nro de cuenta ya existia");
        }

        if(!newCuenta.hasValidEstado()){
            throw new RuntimeException("El valor de estado '" + newCuenta.getEstado() + "' no está permitido. Use 'ACTIVO' o 'CERRADO'");
        }

    }

    @Override
    public List<Cuenta> findAllCuentas() {
        return cuentaRepositoryPort.findAll();
    }

    @Override
    public Cuenta findCuentaById(String cuentaId) {
        return cuentaRepositoryPort.findById(cuentaId);
    }

    @Override
    public List<Cuenta> findCuentaByNroCuenta(String nroCuenta) {
        return cuentaRepositoryPort.findByNroCuenta(nroCuenta);
    }

    @Override
    public List<Cuenta> findCuentaByNombreCliente(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("El nombre del cliente no puede ser vacío");
        }
        // 1. Buscar todos los clientes que coincidan con el nombre
        List<Cliente> clientes = clienteRepositoryPort.findByNameContaining(nombre.trim());

        if (clientes == null || clientes.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. Por cada cliente, buscar sus cuentas y acumularlas
        List<Cuenta> cuentas = new ArrayList<>();
        for (Cliente cliente : clientes) {
            List<Cuenta> cuentasCliente = cuentaRepositoryPort.findByClienteId(cliente.getClienteId());
            cuentas.addAll(cuentasCliente);
        }
        return cuentas;
    }

    @Override
    public Cuenta updateCuenta(String cuentaId, Cuenta cuenta) {
        return null;
    }

    @Override
    public void deleteCuenta(String cuentaId) {
        if(cuentaId == null || cuentaId.trim().isEmpty()){
            throw  new RuntimeException("Id de cuenta a borrar invalido");
        }

        if(cuentaRepositoryPort.findById(cuentaId) == null){
            throw new RuntimeException("Cuenta no existe");
        }

        cuentaRepositoryPort.deleteCuenta(cuentaId);
    }
}
