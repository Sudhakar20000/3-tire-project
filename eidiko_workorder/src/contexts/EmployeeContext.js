import React, { createContext, useState } from "react";

export const EmployeeContext = createContext();

export const EmployeeProvider = ({ children }) => {
  const [refresh, setRefresh] = useState(false);

  const triggerRefresh = () => {
    setRefresh((prev) => !prev); 
  };

  return (
    <EmployeeContext.Provider value={{ refresh, triggerRefresh }}>
      {children}
    </EmployeeContext.Provider>
  );
};
