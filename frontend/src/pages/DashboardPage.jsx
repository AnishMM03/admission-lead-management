import { apiGet, apiPost, apiPut } from '../api';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

function DashboardPage() {
  const [dashboard, setDashboard] = useState(null);
  const [leads, setLeads] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      apiGet('/api/dashboard'),
      apiGet('/api/leads')
    ])
      .then(([dashboardData, leadData]) => {
        setDashboard(dashboardData);
        setLeads(leadData.slice(0, 6));
      })
      .catch((error) => {
        console.error('dashboard fetch failed', error);
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="page"><p>Loading dashboard...</p></div>;

  return (
    <div className="page">
      <div className="topbar">
        <h1>Dashboard</h1>
      </div>

      {dashboard && (
        <div className="card-grid">
          <div className="card">
            <span className="label">Total Leads</span>
            <div className="big">{dashboard.totalLeads}</div>
          </div>
          <div className="card">
            <span className="label">New Leads</span>
            <div className="big">{dashboard.newLeads}</div>
          </div>
          <div className="card">
            <span className="label">Qualified</span>
            <div className="big">{dashboard.qualifiedLeads}</div>
          </div>
          <div className="card">
            <span className="label">Hot Leads</span>
            <div className="big">{dashboard.hotLeads}</div>
          </div>
          <div className="card">
            <span className="label">Converted</span>
            <div className="big">{dashboard.convertedLeads}</div>
          </div>
          <div className="card">
            <span className="label">Overdue Follow-ups</span>
            <div className="big">{dashboard.overdueFollowUps}</div>
          </div>
        </div>
      )}

      <div className="table-wrap">
        <table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Status</th>
              <th>Counsellor</th>
              <th>Source</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {leads.map((lead) => (
              <tr key={lead.id}>
                <td>{lead.fullName}</td>
                <td><span className={`status-badge status-${lead.status}`}>{lead.status}</span></td>
                <td>{lead.assignedCounselorName || 'Unassigned'}</td>
                <td>{lead.sourceName}</td>
                <td>
                  <Link className="link-btn" to={`/leads/${lead.id}`}>View</Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default DashboardPage;
