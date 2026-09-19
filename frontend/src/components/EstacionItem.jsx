// EstacionItem.jsx
// Componente de funcion que representa una sola estacion de trabajo
// (nombre y descripcion), con botones para editar y eliminar. Aisla
// la logica de un unico registro, facilitando su reutilizacion y
// prueba individual.

function EstacionItem({ estacion, onEditar, onEliminar }) {
  return (
    <li>
      <strong>{estacion.nombre}</strong>
      {estacion.descripcion && <span> - {estacion.descripcion}</span>}
      <button onClick={() => onEditar(estacion)}>Editar</button>
      <button onClick={() => onEliminar(estacion.id)}>Eliminar</button>
    </li>
  );
}

export default EstacionItem;
