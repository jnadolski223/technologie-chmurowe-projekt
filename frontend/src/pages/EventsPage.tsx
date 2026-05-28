import { useEffect, useState } from 'react';
import type { EventData } from '../interfaces/EventData.ts';
import type { ApiResponseWrapper } from '../interfaces/ApiResponseWrapper.ts';
import EventList from '../components/EventList.tsx';
import { config } from '../config/config.ts';

export default function EventsPage() {
  const [events, setEvents] = useState<EventData[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  
  useEffect((): void => {
    async function fetchEvents() {
      try {
        setIsLoading(true);
        const response: Response = await fetch(`${config.apiBaseUrl}/events`);
        if (!response.ok) {
          let errorMessage: string = `Błąd serwera: (Status: ${response.status})`;
          
          try {
            const errorResult: ApiResponseWrapper<never> = await response.json();
            if (errorResult.message) {
              errorMessage = errorResult.message;
            }
          } catch { /* empty */ }
          
          setError(errorMessage);
          return;
        }
        
        const result: ApiResponseWrapper<EventData[]> = await response.json();
        if (!result.data) {
          setError('Otrzymano pustą odpowiedź z serwera.');
          return;
        }
        
        setEvents(result.data);
      } catch (err) {
        if (err instanceof Error) {
          setError(`Wystąpił błąd sieci: ${err.message}`);
        } else {
          setError('Wystąpił nieznany błąd aplikacji.');
        }
      } finally {
        setIsLoading(false);
      }
    }

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