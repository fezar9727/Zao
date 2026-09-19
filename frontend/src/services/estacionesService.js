// estacionesService.js
// Modulo de servicio que centraliza la comunicacion HTTP con la API REST
// del modulo de estaciones de trabajo (backend Spring Boot, AA3-EV01).

const BASE_URL = "http://localhost:8091/api/estaciones";

// Obtiene el listado completo de estaciones de trabajo
export async function listarEstaciones() {
  const respuesta = await fetch(BASE_URL);
  if (!respuesta.ok) {
    throw new Error("No se pudo obtener el listado de estaciones");
  }
  return respuesta.json();
}

// Crea una nueva estacion de trabajo
export async function crearEstacion(datos) {
  const respuesta = await fetch(BASE_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(datos),
  });
  if (!respuesta.ok) {
    throw new Error("No se pudo crear la estacion");
  }
  return respuesta.json();
}

// Actualiza una estacion de trabajo existente por su id
export async function actualizarEstacion(id, datos) {
  const respuesta = await fetch(`${BASE_URL}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(datos),
  });
  if (!respuesta.ok) {
    throw new Error("No se pudo actualizar la estacion");
  }
  return respuesta.json();
}

// Elimina una estacion de trabajo por su id
export async function eliminarEstacion(id) {
  const respuesta = await fetch(`${BASE_URL}/${id}`, {
    method: "DELETE",
  });
  if (!respuesta.ok) {
    throw new Error("No se pudo eliminar la estacion");
  }
}
