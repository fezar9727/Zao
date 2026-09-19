// EstacionForm.jsx
// Componente de funcion con estado propio (useState) para los campos
// controlados "nombre" y "descripcion". Se usa tanto para crear una
// estacion nueva como para editar una existente: si recibe una estacion
// por props, precarga el formulario con sus datos (modo edicion); si no,
// arranca vacio (modo creacion).

import { useState, useEffect } from "react";

function EstacionForm({ estacionSeleccionada, onGuardar, onCancelar }) {
  const [nombre, setNombre] = useState("");
  const [descripcion, setDescripcion] = useState("");

  useEffect(() => {
    if (estacionSeleccionada) {
      setNombre(estacionSeleccionada.nombre);
      setDescripcion(estacionSeleccionada.descripcion || "");
    } else {
      setNombre("");
      setDescripcion("");
    }
  }, [estacionSeleccionada]);

  function manejarEnvio(evento) {
    evento.preventDefault();
    onGuardar({ nombre, descripcion });
  }

  return (
    <form onSubmit={manejarEnvio}>
      <label>
        Nombre
        <input
          type="text"
          value={nombre}
          onChange={(evento) => setNombre(evento.target.value)}
          required
        />
      </label>

      <label>
        Descripcion
        <textarea
          value={descripcion}
          onChange={(evento) => setDescripcion(evento.target.value)}
        />
      </label>

      <div>
        <button type="submit">
          {estacionSeleccionada ? "Guardar cambios" : "Crear estacion"}
        </button>
        {estacionSeleccionada && (
          <button type="button" onClick={onCancelar}>
            Cancelar
          </button>
        )}
      </div>
    </form>
  );
}

export default EstacionForm;
