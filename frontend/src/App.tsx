import { BrowserRouter, NavLink, Route, Routes } from 'react-router-dom';
import HomePage from './pages/HomePage.tsx';
import LoginPage from './pages/LoginPage.tsx';
import RegisterPage from './pages/RegisterPage.tsx';
import EventLayout from "./pages/events/EventLayout.tsx";
import EventsPage from './pages/events/EventsPage.tsx';
import EventDetailsPage from './pages/events/EventDetailsPage.tsx';
import EventFormPage from './pages/events/EventFormPage.tsx';
import AccountLayout from './pages/account/AccountLayout.tsx';
import AccountProfilePage from './pages/account/AccountProfilePage.tsx';
import AccountFormPage from './pages/account/AccountFormPage.tsx';
import AccountEventsPage from './pages/account/AccountEventsPage.tsx';
import AccountBookingsPage from './pages/account/AccountBookingsPage.tsx';
import NotFoundPage from './pages/NotFoundPage.tsx';

export default function App() {
  return (
    <BrowserRouter>
      <nav>
        <NavLink to="/">Strona Główna</NavLink>
        <NavLink to="/events">Wydarzenia</NavLink>
        <NavLink to="/account">Konto</NavLink>
      </nav>

      <hr/>

      <Routes>
        <Route path="/" element={<HomePage/>}/>
        <Route path="/login" element={<LoginPage/>}/>
        <Route path="/register" element={<RegisterPage/>}/>

        <Route path="/events" element={<EventLayout/>}>
          <Route index element={<EventsPage/>}/>
          <Route path="form" element={<EventFormPage/>}/>
          <Route path=":eventId" element={<EventDetailsPage/>}/>
          <Route path=":eventId/form" element={<EventFormPage/>}/>
        </Route>
        <Route path="/account" element={<AccountLayout/>}>
          <Route path="profile" element={<AccountProfilePage/>}/>
          <Route path="form" element={<AccountFormPage/>}/>
          <Route path="events" element={<AccountEventsPage/>}/>
          <Route path="bookings" element={<AccountBookingsPage/>}/>
        </Route>
        <Route path="*" element={<NotFoundPage/>}/>
      </Routes>
    </BrowserRouter>
  )
}
