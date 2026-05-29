import { useEffect, useState } from 'react';
import { type NavigateFunction, useNavigate } from 'react-router-dom';
import EventList from '../../components/EventList.tsx';
import { config } from '../../config/config.ts';
import type { ApiResponse, EventData, UserStorageData } from '../../interfaces';

export default function AccountEventsPage() {
  const navigate: NavigateFunction = useNavigate();

  const [events, setEvents] = useState<EventData[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect((): void => {
    const fetchEvents: () => Promise<void> = async (): Promise<void> => {
      const errorMessage: string = 'Błąd: Nie udało się pobrać listy wydarzeń';

      try {
        setIsLoading(true);
        setError(null);

        const userDataRaw: string | null = localStorage.getItem(config.userStorageKey);
        if (!userDataRaw) {
          alert('Błąd: Musisz być zalogowany, aby zobaczyć tę stronę.');
          navigate('/login');
          return;
        }

        const loggedInUser: UserStorageData = JSON.parse(userDataRaw);
        const userId: string = loggedInUser.id;

        const response: Response = await fetch(`${config.apiBaseUrl}/users/${userId}/events`);
        if (!response.ok) {
          const apiErrorData: ApiResponse<never> = await response.json().catch((): null => null);
          console.error('Błąd API:', { status: response.status, apiResponse: apiErrorData });
          setError(errorMessage);
          return;
        }

        const result: ApiResponse<EventData[]> = await response.json();
        if (!result.data) {
          console.error('Błąd API: Zwrócono status 200, ale brakuje danych:', result);
          setError(errorMessage);
          return;
        }

        setEvents(result.data);
      } catch (err) {
        console.error('Błąd sieciowy / krytyczny aplikacji:', err);
        setError(errorMessage);
      } finally {
        setIsLoading(false);
      }
    };

    fetchEvents().catch((err: unknown): void => {
      console.error(`Nieobsłużony błąd w fetchEvents: ${err}`);
    });
  }, [navigate]);

  if (isLoading) {
    return (<p>Ładowanie wydarzeń...</p>);
  }

  if (error) {
    return (
      <div>
        <h3>Wystąpił problem</h3>
        <p>{error}</p>
      </div>
    );
  }

  return (
    <div>
      <h1>Moje wydarzenia</h1>
      <EventList events={events}/>
    </div>
  );
}
