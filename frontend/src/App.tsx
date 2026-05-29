import { BrowserRouter, Link, Route, Routes } from 'react-router-dom';
import HomePage from './pages/HomePage.tsx';
import NotFoundPage from './pages/NotFoundPage.tsx';
import EventsPage from './pages/EventsPage.tsx';
import EventDetailsPage from './pages/EventDetailsPage.tsx';
import EventFormPage from './pages/EventFormPage.tsx';

export default function App() {
  return (
    <BrowserRouter>
      <nav>
        <Link to="/">Strona Główna</Link>
        <Link to="/events">Wydarzenia</Link>
        <Link to="/account">Konto</Link>
      </nav>

      <Routes>
        <Route path="/" element={<HomePage/>}/>
        <Route path="/events" element={<EventsPage/>}/>
        <Route path="/events/form" element={<EventFormPage/>}/>
        <Route path="/events/:eventId" element={<EventDetailsPage/>}/>
        <Route path="/events/:eventId/form" element={<EventFormPage/>}/>
        <Route path="*" element={<NotFoundPage/>}/>
      </Routes>
    </BrowserRouter>
  )
}
