import { useEffect, useState } from 'react';
import type { EventData } from '../interfaces/EventData.ts';
import type { ApiResponse } from '../interfaces/ApiResponse.ts';
import EventList from '../components/EventList.tsx';
import { config } from '../config/config.ts';

export default function EventsPage() {
  const [events, setEvents] = useState<EventData[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  
  useEffect((): void => {
    const fetchEvents: () => Promise<void> = async (): Promise<void> => {
      const errorMessage: string = 'Błąd: Nie udało się pobrać listy wydarzeń';

      try {
        setIsLoading(true);
        setError(null);

        const response: Response = await fetch(`${config.apiBaseUrl}/events`);
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
  }, []);

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
      <h1>Wydarzenia</h1>
      <EventList events={events}/>
    </div>
  );
}