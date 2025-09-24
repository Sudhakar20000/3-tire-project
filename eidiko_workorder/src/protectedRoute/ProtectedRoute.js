import React, { useEffect, useState } from 'react';
import { Navigate, useLocation, useNavigate } from 'react-router-dom';

const ProtectedRoute = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(null);
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    const token = localStorage.getItem('token');

    if (!token) {
      setIsAuthenticated(false);
    } else {
      setIsAuthenticated(true);
    }

    // Prevent back navigation if not authenticated
    window.history.pushState(null, '', window.location.href);
    window.onpopstate = () => {
      const tokenCheck = localStorage.getItem('token');
      if (!tokenCheck) {
        navigate('/eidiko_workorder/', { replace: true });
      } else {
        window.history.go(1);
      }
    };

    return () => {
      window.onpopstate = null;
    };
  }, [navigate]);

  // While checking auth status
  if (isAuthenticated === null) {
    return null; // or a loading spinner
  }

  // If not authenticated
  if (!isAuthenticated) {
    return <Navigate to="/eidiko_workorder/" state={{ from: location }} replace />;
  }

  // Authenticated: show child routes
  return children;
};

export default ProtectedRoute;
