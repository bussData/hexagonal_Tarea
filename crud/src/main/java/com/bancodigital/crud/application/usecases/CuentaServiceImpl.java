package com.bancodigital.crud.application.usecases;

import com.bancodigital.crud.application.ports.input.CuentaService;
import com.bancodigital.crud.application.ports.output.ClienteRepositoryPort;
import com.bancodigital.crud.application.ports.output.CuentaRepositoryPort;
import com.bancodigital.crud.domain.model.Cliente;
import com.bancodigital.crud.domain.model.Cuenta;
import com.bancodigital.crud.infraestructure.adapters.output.entities.ClienteEntity;
import com.bancodigital.crud.infraestructure.adapters.output.entities.CuentaEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    //validaciones de negocio:
    //saldo >= 0
    //numero_cuenta único
    //estado valores permitidos: 'ACTIVO', 'CERRADO'
    private void validacionesRegistroCuenta(Cuenta newCuenta) {
        if(new BigDecimal(0).compareTo(newCuenta.getSaldo())==0
                || newCuenta.getSaldo().compareTo(new BigDecimal(0))<0){
            throw new RuntimeException("El monto del saldo no puede ser menor o igual a cero");
        }

        if(!cuentaRepositoryPort.esCuentaUnica(newCuenta)){
            throw new RuntimeException("Nro de cuenta ya existia");
        }

        if(newCuenta.getEstado().equals("ACTIVA") || newCuenta.getEstado().equals("CERRADO")){
           //esta ok
        }else{
            throw new RuntimeException("El valor de estado "+newCuenta.getEstado()+" no esta permitido");
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
