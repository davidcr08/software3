package uniquindio.product.services.interfaces;

import uniquindio.product.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteService {
    Cliente crearCliente(Cliente cliente);
    Optional<Cliente> obtenerCliente(String id);
    Optional<Cliente> obtenerClientePorCedula(String cedula);
    Optional<Cliente> obtenerClientePorEmail(String email);
    Cliente actualizarCliente(Cliente cliente);
    void eliminarCliente(String id);
    List<Cliente> listarClientes();
}