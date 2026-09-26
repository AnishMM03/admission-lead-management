import { useState } from 'react';
import { apiPost } from '../api';
import { useNavigate } from 'react-router-dom';

function AddLeadPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    fullName: '',
    email: '',
    phone: '',
    city: '',
    address: '',
    sourceName: 'Website',
    sourceChannel: 'Website',
    coursePreferences: 'B.Tech, BBA',
    assignedCounselorId: ''
  });
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);

    const payload = {
      fullName: form.fullName,
      email: form.email,
      phone: form.phone,
      city: form.city,
      address: form.address,
      sourceName: form.sourceName,
      sourceChannel: form.sourceChannel,
      coursePreferences: form.coursePreferences
        .split(',')
        .map(item => item.trim())
        .filter(Boolean),
      assignedCounselorId: form.assignedCounselorId ? Number(form.assignedCounselorId) : null
    };

    try {
      await apiPost('/api/leads', payload);
      navigate('/leads');
    } catch (error) {
      console.error('Lead creation failed', error);
      alert('Failed to create lead');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="page">
      <div className="topbar">
        <h1>Add New Lead</h1>
      </div>

      <div className="form-panel">
        <form onSubmit={handleSubmit}>
          <div className="form-grid">
            <div className="form-field">
              <label>Full Name</label>
              <input name="fullName" value={form.fullName} onChange={handleChange} required />
            </div>
            <div className="form-field">
              <label>Email</label>
              <input type="email" name="email" value={form.email} onChange={handleChange} required />
            </div>
            <div className="form-field">
              <label>Phone</label>
              <input name="phone" value={form.phone} onChange={handleChange} required />
            </div>
            <div className="form-field">
              <label>City</label>
              <input name="city" value={form.city} onChange={handleChange} />
            </div>
            <div className="form-field">
              <label>Lead Source</label>
              <select name="sourceName" value={form.sourceName} onChange={handleChange}>
                <option value="Website">Website</option>
                <option value="WhatsApp">WhatsApp</option>
                <option value="Walk-in">Walk-in</option>
                <option value="Fair">Fair</option>
                <option value="Phone">Phone</option>
              </select>
            </div>
            <div className="form-field">
              <label>Channel</label>
              <input name="sourceChannel" value={form.sourceChannel} onChange={handleChange} />
            </div>
            <div className="form-field">
              <label>Assigned Counsellor ID</label>
              <input name="assignedCounselorId" type="number" value={form.assignedCounselorId} onChange={handleChange} />
            </div>
            <div className="form-field" style={{ gridColumn: '1 / -1' }}>
              <label>Course Preferences</label>
              <input name="coursePreferences" value={form.coursePreferences} onChange={handleChange} />
            </div>
            <div className="form-field" style={{ gridColumn: '1 / -1' }}>
              <label>Address</label>
              <textarea name="address" value={form.address} onChange={handleChange} />
            </div>
          </div>

          <div className="actions">
            <button className="primary-btn" type="submit" disabled={submitting}>
              {submitting ? 'Saving...' : 'Save Lead'}
            </button>
            <button className="secondary-btn" type="button" onClick={() => navigate('/leads')}>Cancel</button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default AddLeadPage;
