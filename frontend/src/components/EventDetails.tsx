import { type NavigateFunction, useNavigate } from 'react-router-dom';
import { config } from '../config/config.ts';
import type { ApiResponse, EventData, UserStorageData } from '../interfaces';

interface EventDetailsProps {
  event: EventData;
  bookingCount: number;
  isOwner: boolean;
  bookingId: string | null;
}

export default function EventDetails({ event, bookingCount, isOwner, bookingId }: EventDetailsProps) {
  const navigate: NavigateFunction = useNavigate();

  const handleBack = (): void => {
    navigate(-1);
  };

  const handleEdit = (): void => {
    navigate(`/events/${event.id}/form`);
  }

  const handleDelete = async (): Promise<void> => {
    const errorMessage: string = 'Błąd: Nie udało się usunąć wydarzenia'
    const confirmed: boolean = window.confirm('Czy na pewno chcesz usunąć to wydarzenie? Wszystkie dane i zapisy na to wydarzenie zostaną usunięte.');
    if (confirmed) {
      try {
        const response: Response = await fetch(`${config.apiBaseUrl}/events/${event.id}`, { method: 'DELETE' });
        if (response.ok) {
          alert('Wydarzenie zostało usunięte.');
          navigate('/events');
        } else {
          const apiErrorData: ApiResponse<never> = await response.json().catch((): null => null);
          console.error('Błąd API:', { status: response.status, apiResponse: apiErrorData });
          alert(errorMessage);
        }
      } catch (err) {
        console.error('Błąd sieciowy / krytyczny aplikacji:', err);
        alert(errorMessage);
      }
    }
  }

  const handleBooking = async (): Promise<void> => {
    try {
      if (bookingId === null) {
        const userDataRaw: string | null = localStorage.getItem(config.userStorageKey);
        if (!userDataRaw) {
          alert('Błąd: Musisz być zalogowany, aby zapisać się na wydarzenie.');
          navigate('/login');
          return;
        }

        const userData: UserStorageData = JSON.parse(userDataRaw);
        const userId: string = userData.id;

        const response: Response = await fetch(`${config.apiBaseUrl}/bookings`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ userId: userId, eventId: event.id })
        });

        if (response.ok) {
          alert('Serwis przyjął zgłoszenie zapisu - może to potrwać chwilę.');
          navigate('/account/bookings');
        } else {
          const apiErrorData: ApiResponse<never> = await response.json().catch((): null => null);
          console.error('Błąd API:', { status: response.status, apiResponse: apiErrorData });
          alert('Błąd: Nie udało się zapisać na wydarzenie.');
        }
      } else {
        const response: Response = await fetch(`${config.apiBaseUrl}/bookings/${bookingId}`, { method: 'DELETE' });
        if (response.ok) {
          alert('Serwis przyjął zgłoszenie wypisu - może to potrwać chwilę.');
          navigate('/events');
        } else {
          const apiErrorData: ApiResponse<never> = await response.json().catch((): null => null);
          console.error('Błąd API:', { status: response.status, apiResponse: apiErrorData });
          alert('Błąd: Nie udało się wypisać z wydarzenia.');
        }
      }
    } catch (err) {
      console.error('Błąd sieciowy / krytyczny aplikacji:', err);
      alert('Błąd: Nie udało się wykonać operacji.');
    }
  }

  return (
    <div>
      <div>
        <h2>{event.title}</h2>
        <p><strong>Data wydarzenia:</strong> {event.date}</p>
        <p><strong>Godzina startu wydarzenia:</strong> {event.time}</p>
        <p><strong>Miejsce wydarzenia:</strong> {event.location}</p>
        <p><strong>Liczba zapisanych osób:</strong> {bookingCount}</p>
        <h2>Opis wydarzenia</h2>
        <p>{event.description}</p>
      </div>
      <div>
        <button onClick={handleBack}>Wróć</button>

        {isOwner ? (
          <>
            <button onClick={handleEdit}>Edytuj</button>
            <button onClick={handleDelete}>Usuń</button>
          </>
        ) : (
          <button onClick={handleBooking}>{bookingId ? 'Wypisz się' : 'Zapisz się'}</button>
        )}
      </div>
    </div>
  )
}
