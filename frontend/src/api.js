import { useEffect, useState } from 'react';
import { apiGet } from '../api';

function CounselorsPage() {
  const [counselors, setCounselors] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    apiGet('/api/counselors')
      .then((data) => setCounselors(data))
      .catch((error) => console.error('Failed to fetch counselors', error))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="page"><p>Loading counsellors...</p></div>;

  return (
    <div className="page">
      <div className="topbar">
        <h1>Counsellors</h1>
      </div>

      <div className="table-wrap">
        <table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Email</th>
              <th>Phone</th>
              <th>Department</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {counselors.map((counselor) => (
              <tr key={counselor.id}>
                <td>{counselor.name}</td>
                <td>{counselor.email}</td>
                <td>{counselor.phone}</td>
                <td>{counselor.department || '—'}</td>
                <td>{counselor.active ? 'Active' : 'Inactive'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default CounselorsPage;
