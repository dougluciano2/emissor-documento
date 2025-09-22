import { Routes, Route, Link } from 'react-router-dom';
import { PersonManagementPage } from './pages/PersonManagementPage';
import { DocumentEditorPage } from './pages/DocumentEditorPage';
import MedicalRecordPage from './pages/MedicalRecordPage';
import TemplateEditorPage from './pages/TemplateEditorPage';

function App() {
  return (
    <div>
      {/* Menu de Navegação Simples */}
      <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
        <div className="container-fluid">
          <Link className="navbar-brand" to="/">Emissor de Documentos</Link>
          <div className="navbar-nav">
            <Link className="nav-link" to="/">Gerenciar Pessoas</Link>
             <Link className="nav-link" to="/templates/editor">Gerenciar Modelos</Link>
          </div>
        </div>
      </nav>

      {/* Área onde as páginas serão renderizadas */}
      <main>
        <Routes>
          <Route path="/" element={<PersonManagementPage />} />
          <Route path="/document/editor/:personId" element={<DocumentEditorPage />} />
          <Route path="/medical-record/:personId" element={<MedicalRecordPage />}  />
          <Route path="/templates/editor" element={<TemplateEditorPage />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;