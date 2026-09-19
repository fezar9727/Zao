// App.jsx
// Componente raiz de la aplicacion. Aloja al componente EstacionesPage,
// que orquesta toda la vista del modulo de estaciones de trabajo. Toda
// aplicacion React necesita un punto de entrada unico desde el cual se
// despliega el arbol de componentes.

import EstacionesPage from "./components/EstacionesPage";

function App() {
  return (
    <div className="App">
      <EstacionesPage />
    </div>
  );
}

export default App;
