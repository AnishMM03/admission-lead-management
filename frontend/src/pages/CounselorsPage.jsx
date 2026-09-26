import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { apiGet, apiPost, apiPut } from '../api';

function LeadDetailPage() {
  const { id } = useParams();
  const [lead, setLead] = useState(null);
  const [followUps, setFollowUps] = useState([]);
  const [loading, setLoading] = useState(true);
  const [status, setStatus] = useState('NEW');
  const [followUpForm, setFollowUpForm] = useState({
    type: 'CALL',
    status: 'PENDING',
    notes: '',
    scheduledAt: ''
  });

  const loadData = async () => {
    try {
      const [leadData, followUpData] = await Promise.all([
        apiGet(`/api/leads/${id}`),
        apiGet(`/api/leads/${id}/followups`)
      ]);
      setLead(leadData);
      setStatus(leadData.status);
      setFollowUps(followUpData);
    } catch (error) {
      console.error('Failed to fetch lead data', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, [id]);

  const handleStatusSubmit = async (e) => {
    e.preventDefault();
    try {
      await apiPut(`/api/leads/${id}/status`, { status });
      await loadData();
    } catch (error) {
      console.error('Status update failed', error);
      alert('Status update failed');
    }
  };

  const handleFollowUpSubmit = async (e) => {
    e.preventDefault();
    try {
      await apiPost(`/api/leads/${id}/followups`, {
        ...followUpForm,
        scheduledAt: followUpForm.scheduledAt ? new Date(followUpForm.scheduledAt).toISOString() : null
      });
      setFollowUpForm({ type: 'CALL', status: 'PENDING', notes: '', scheduledAt: '' });
      await loadData();
    } catch (error) {
      console.error('Follow-up creation failed', error);
      alert('Failed to create follow-up');
    }
  };

  if (loading) return <div className="page"><p>Loading lead details...</p></div>;
  if (!lead) return <div className="page"><p>Lead not found.</p></div>;

  return (
    <div className="page">
      <div className="topbar">
        <h1>{lead.fullName}</h1>
      </div>

      <div className="detail-grid">
        <div className="list-stack">
          <div className="info-box">
            <h3>Lead Information</h3>
            <div className="meta-row"><span>Email</span><strong>{lead.email}</strong></div>
            <div className="meta-row"><span>Phone</span><strong>{lead.phone}</strong></div>
            <div className="meta-row"><span>City</span><strong>{lead.city || 'N/A'}</strong></div>
            <div className="meta-row"><span>Source</span><strong>{lead.sourceName}</strong></div>
            <div className="meta-row"><span>Counsellor</span><strong>{lead.assignedCounselorName || 'Unassigned'}</strong></div>
            <div className="meta-row"><span>Courses</span><strong>{lead.coursePreferences?.join(', ') || 'N/A'}</strong></div>
          </div>

          <div className="info-box">
            <h3>Follow-up History</h3>
            {followUps.length === 0 ? (
              <p className="empty-state">No follow-up history yet.</p>
            ) : (
              <div className="list-stack">
                {followUps.map((item) => (
                  <div key={item.id} className="info-box" style={{ padding: '12px 14px', background: '#f8fafc' }}>
                    <div className="meta-row"><span>Type</span><strong>{item.type}</strong></div>
                    <div className="meta-row"><span>Status</span><strong>{item.status}</strong></div>
                    <div className="meta-row"><span>Scheduled</span><strong>{item.scheduledAt || 'N/A'}</strong></div>
                    <div className="meta-row"><span>Notes</span><strong>{item.notes || 'N/A'}</strong></div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>

        <div className="list-stack">
          <div className="form-panel">
            <h3>Update Status</h3>
            <form onSubmit={handleStatusSubmit}>
              <div className="form-field">
                <label>Status</label>
                <select value={status} onChange={(e) => setStatus(e.target.value)}>
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
              <div className="actions">
                <button className="primary-btn" type="submit">Update</button>
              </div>
            </form>
          </div>

          <div className="form-panel">
            <h3>Add Follow-up</h3>
            <form onSubmit={handleFollowUpSubmit}>
              <div className="form-grid">
                <div className="form-field">
                  <label>Type</label>
                  <select value={followUpForm.type} onChange={(e) => setFollowUpForm({ ...followUpForm, type: e.target.value })}>
                    <option value="CALL">CALL</option>
                    <option value="WHATSAPP">WHATSAPP</option>
                    <option value="EMAIL">EMAIL</option>
                    <option value="WALKIN">WALKIN</option>
                    <option value="FAIR">FAIR</option>
                    <option value="VISIT">VISIT</option>
                    <option value="OTHER">OTHER</option>
                  </select>
                </div>
                <div className="form-field">
                  <label>Status</label>
                  <select value={followUpForm.status} onChange={(e) => setFollowUpForm({ ...followUpForm, status: e.target.value })}>
                    <option value="PENDING">PENDING</option>
                    <option value="COMPLETED">COMPLETED</option>
                    <option value="MISSED">MISSED</option>
                    <option value="RESCHEDULED">RESCHEDULED</option>
                  </select>
                </div>
                <div className="form-field" style={{ gridColumn: '1 / -1' }}>
                  <label>Scheduled Time</label>
                  <input type="datetime-local" value={followUpForm.scheduledAt} onChange={(e) => setFollowUpForm({ ...followUpForm, scheduledAt: e.target.value })} />
                </div>
                <div className="form-field" style={{ gridColumn: '1 / -1' }}>
                  <label>Notes</label>
                  <textarea value={followUpForm.notes} onChange={(e) => setFollowUpForm({ ...followUpForm, notes: e.target.value })} />
                </div>
              </div>

              <div className="actions">
                <button className="primary-btn" type="submit">Save Follow-up</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default LeadDetailPage;
