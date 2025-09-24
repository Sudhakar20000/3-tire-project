import { useEffect } from 'react';
import { useLocation, useNavigationType, useNavigate } from 'react-router-dom';

function useHandleBackNavigation() {
  const location = useLocation();
  const navigationType = useNavigationType();
  const navigate = useNavigate();

  useEffect(() => {
    // Check navigation type from Performance API
    const navEntries = performance.getEntriesByType('navigation');
    const navType = navEntries.length > 0 ? navEntries[0].type : null;

    // navType can be 'reload', 'back_forward', or 'navigate'

    if (navType === 'back_forward' && navigationType === 'POP') {
      // This is a back/forward browser navigation
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      navigate('/eidiko_workorder/', { replace: true });
    }
    // else: do nothing on reload or fresh navigate

  }, [location, navigationType, navigate]);
}

export default useHandleBackNavigation;
