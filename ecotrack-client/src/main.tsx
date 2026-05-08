import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import {UserProvider} from "./context/UserContext.tsx";
import {ThemeProvider} from "./context/ThemeContext.tsx";
import {ErrorProvider} from "./context/ErrorContext.tsx";

createRoot(document.getElementById('root')!).render(
  <StrictMode>
      <UserProvider>
          <ThemeProvider>
              <ErrorProvider>
                  <App />
              </ErrorProvider>
          </ThemeProvider>
      </UserProvider>
  </StrictMode>,
)
