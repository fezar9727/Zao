// EstacionesPage.jsx
// Componente de pagina que orquesta la vista completa del modulo de
// estaciones de trabajo. Mantiene en su estado la lista de estaciones
// obtenida de la API (useState) y dispara la peticion inicial de datos
// al montarse (useEffect). Centraliza el estado compartido entre el
// listado (EstacionList) y el formulario (EstacionForm).

import { useState, useEffect } from "react";
import EstacionList from "./EstacionList";
import EstacionForm from "./EstacionForm";
import {
  listarEstaciones,
  crearEstacion,
  actualizarEstacion,
  eliminarEstacion,
} from "../services/estacionesService";

function EstacionesPage() {
  const [estaciones, setEstaciones] = useState([]);
  const [estacionSeleccionada, setEstacionSeleccionada] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    cargarEstaciones();
  }, []);

  async function cargarEstaciones() {
    try {
      const datos = await listarEstaciones();
      setEstaciones(datos);
      setError("");
    } catch (err) {
      setError("No se pudo cargar el listado de estaciones");
    }
  }

  async function manejarGuardar(datosFormulario) {
    try {
      if (estacionSeleccionada) {
        await actualizarEstacion(estacionSeleccionada.id, datosFormulario);
      } else {
        await crearEstacion(datosFormulario);
      }
      setEstacionSeleccionada(null);
      await cargarEstaciones();
    } catch (err) {
      setError("No se pudo guardar la estacion");
    }
  }

  async function manejarEliminar(id) {
    try {
      await eliminarEstacion(id);
      await cargarEstaciones();
    } catch (err) {
      setError("No se pudo eliminar la estacion");
    }
  }

  function manejarCancelar() {
    setEstacionSeleccionada(null);
  }

  return (
    <section>
      <h1>Estaciones de trabajo</h1>

      {error && <p>{error}</p>}

      <EstacionForm
        estacionSeleccionada={estacionSeleccionada}
        onGuardar={manejarGuardar}
        onCancelar={manejarCancelar}
      />

      <EstacionList
        estaciones={estaciones}
        onEditar={setEstacionSeleccionada}
        onEliminar={manejarEliminar}
      />
    </section>
  );
}

export default EstacionesPage;
