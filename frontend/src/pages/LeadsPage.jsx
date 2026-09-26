import { useEffect, useState } from 'react';
import { apiGet, apiPost, apiPut } from '../api';
import { Link } from 'react-router-dom';

function LeadsPage() {
  const [leads, setLeads] = useState([]);
  const [loading, setLoading] = useState(true);
  const [statusFilter, setStatusFilter] = useState('ALL');

  const loadLeads = () => {
    apiGet('/api/leads')
      .then((data) => setLeads(data))
      .catch((error) => console.error('Failed to fetch leads', error))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadLeads();
  }, []);

  const filteredLeads = statusFilter === 'ALL'
    ? leads
    : leads.filter((lead) => lead.status === statusFilter);

  if (loading) return <div className="page"><p>Loading leads...</p></div>;

  return (
    <div className="page">
      <div className="topbar">
        <h1>Lead List</h1>
        <Link className="primary-btn" to="/leads/new">+ Add Lead</Link>
      </div>

      <div className="form-panel" style={{ marginBottom: '20px' }}>
        <div className="form-field" style={{ maxWidth: '260px' }}>
          <label>Filter by Status</label>
          <select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)}>
            <option value="ALL">All</option>
            <option value="NEW">NEW</option>
            <option value="CONTACTED">CONTACTED</option>
            <option value="FOLLOW_UP_SCHEDULED">FOLLOW_UP_SCHEDULED</option>
            <option value="QUALIFIED">QUALIFIED</option>
            <option value="HOT">HOT</option>
            <option value="CONVERTED">CONVERTED</option>
            <option value="CLOSED_LOST">CLOSED_LOST</option>
            <option value="INACTIVE">INACTIVE</option>
          </select>
        </div>
      </div>

      <div className="table-wrap">
        <table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Email</th>
              <th>Phone</th>
              <th>Source</th>
              <th>Status</th>
              <th>Counsellor</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {filteredLeads.map((lead) => (
              <tr key={lead.id}>
                <td>{lead.fullName}</td>
                <td>{lead.email}</td>
                <td>{lead.phone}</td>
                <td>{lead.sourceName}</td>
                <td><span className={`status-badge status-${lead.status}`}>{lead.status}</span></td>
                <td>{lead.assignedCounselorName || 'Unassigned'}</td>
                <td>
                  <Link className="link-btn" to={`/leads/${lead.id}`}>Open</Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default LeadsPage;
