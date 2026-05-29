import { NavLink, Outlet } from 'react-router-dom';

export default function AccountLayout() {
  return (
    <div>
      <nav>
        <NavLink to="/account/profile">Moje dane</NavLink>
        <NavLink to="/account/events">Moje wydarzenia</NavLink>
        <NavLink to="/account/bookings">Moje zapisy</NavLink>
      </nav>

      <hr/>

      <div>
        <Outlet/>
      </div>
    </div>
  )
}
