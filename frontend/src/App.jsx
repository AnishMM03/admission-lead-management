import { Routes, Route, NavLink } from 'react-router-dom';
import DashboardPage from './pages/DashboardPage';
import LeadsPage from './pages/LeadsPage';
import CounselorsPage from './pages/CounselorsPage';
import AddLeadPage from './pages/AddLeadPage';
import LeadDetailPage from './pages/LeadDetailPage';

function App() {
  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand">
          <div className="brand-mark">ALM</div>
          <div>
            <strong>Admission</strong>
            <small>Lead Management</small>
          </div>
        </div>

        <nav className="nav">
          <NavLink to="/" end>Dashboard</NavLink>
          <NavLink to="/leads">Leads</NavLink>
          <NavLink to="/leads/new">New Lead</NavLink>
          <NavLink to="/counselors">Counsellors</NavLink>
        </nav>
      </aside>

      <main className="main-content">
        <Routes>
          <Route path="/" element={<DashboardPage />} />
          <Route path="/leads" element={<LeadsPage />} />
          <Route path="/leads/new" element={<AddLeadPage />} />
          <Route path="/leads/:id" element={<LeadDetailPage />} />
          <Route path="/counselors" element={<CounselorsPage />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;
