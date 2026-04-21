import { createContext, useState, useContext } from 'react';

export const SidebarContext = createContext();

export function SidebarProvider({ children }) {
  const [collapsed, setCollapsed] = useState(true); // 🔥 inicia cerrado

  const toggle = () => {
    console.log("TOGGLE CLICK"); // 👈 debug
    setCollapsed(prev => !prev);
  };

  return (
    <SidebarContext.Provider value={{ collapsed, toggle }}>
      {children}
    </SidebarContext.Provider>
  );
}

export function useSidebar() {
  return useContext(SidebarContext);
}