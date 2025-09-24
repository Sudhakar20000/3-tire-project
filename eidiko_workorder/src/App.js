import './App.css';
import { Route, Routes } from 'react-router-dom';
import Login from './pages/Login';
import Layout from './layout/Layout';
import Dashboard from './pages/Dashboard';
import { EmployeeProvider } from './contexts/EmployeeContext';
import SignUp from './pages/SignUp';
import ProtectedRoute from './protectedRoute/ProtectedRoute';
import { Navigate } from 'react-router-dom';
import useHandleBackNavigation from './pages/storageClearing/ClearStorage';
  
function App() {
   useHandleBackNavigation(); 
  return (
    <div className="App">
      <EmployeeProvider>
        <Layout>
          <Routes>
            <Route path="/" element={<Navigate to="/eidiko_workorder/" />} />
            <Route path="/eidiko_workorder/" element={<Login />} />
            <Route path="/eidiko_workorder/signUp" element={<SignUp />} />
            <Route
              path="/eidiko_workorder/dashboard"
              element={
                <ProtectedRoute>
                  <Dashboard />
                </ProtectedRoute>
              }
            />
            {/* 🔻 This handles unknown routes */}
            <Route path="*" element={<div style={{ textAlign: 'center', marginTop: '100px' }}><h1>404 - Page Not Found</h1></div>} />
          </Routes>

        </Layout>
      </EmployeeProvider>
    </div>
  );
}

export default App;
