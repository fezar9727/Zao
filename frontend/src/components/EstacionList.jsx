// EstacionList.jsx
// Componente de funcion que recibe el arreglo completo de estaciones por
// props y lo recorre con map para renderizar un EstacionItem por cada
// registro. Separa la responsabilidad de mostrar la coleccion completa
// de la responsabilidad de mostrar un solo elemento.

import EstacionItem from "./EstacionItem";

function EstacionList({ estaciones, onEditar, onEliminar }) {
  if (estaciones.length === 0) {
    return <p>No hay estaciones de trabajo registradas todavia.</p>;
  }

  return (
    <ul>
      {estaciones.map((estacion) => (
        <EstacionItem
          key={estacion.id}
          estacion={estacion}
          onEditar={onEditar}
          onEliminar={onEliminar}
        />
      ))}
    </ul>
  );
}

export default EstacionList;
