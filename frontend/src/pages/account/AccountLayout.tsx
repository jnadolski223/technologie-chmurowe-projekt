import { useEffect } from 'react';
import { type NavigateFunction, NavLink, Outlet, useNavigate } from 'react-router-dom';
import { config } from '../../config/config.ts';

export default function AccountLayout() {
  const navigate: NavigateFunction = useNavigate();
  const user: string | null = localStorage.getItem(config.userStorageKey);

  useEffect((): void => {
    if (!user) {
      navigate('/login');
    }
  }, [user, navigate]);

  if (!user) {
    return null;
  }

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
