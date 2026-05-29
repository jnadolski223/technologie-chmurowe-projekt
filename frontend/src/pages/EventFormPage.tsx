import { useEffect, useState } from 'react';
import { useParams, useNavigate, type NavigateFunction } from 'react-router-dom';
import EventForm from '../components/EventForm.tsx';
import { config } from '../config/config.ts';
import type { ApiResponse, EventData, UserStorageData } from '../interfaces';

export default function EventFormPage() {
  const { eventId } = useParams<{ eventId: string }>();
  const navigate: NavigateFunction = useNavigate();

  const [event, setEvent] = useState<EventData | null>(null);

  const [isLoading, setIsLoading] = useState<boolean>(!!eventId);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!eventId) {
      return;
    }

    const fetchEventToEdit = async () => {
      const errorMessage: string = 'Błąd: Nie udało się pobrać danych wydarzenia do edycji.';

      try {
        setIsLoading(true);
        setError(null);

        const userDataRaw: string | null = localStorage.getItem(config.userStorageKey);
        if (!userDataRaw) {
          alert('Błąd: Musisz być zalogowany, aby edytować wydarzenie.');
          navigate('/login');
          return;
        }

        const loggedInUser: UserStorageData = JSON.parse(userDataRaw);

        const response: Response = await fetch(`${config.apiBaseUrl}/events/${eventId}`);
        if (!response.ok) {
          const apiErrorData: ApiResponse<never> = await response.json().catch((): null => null);
          console.error('Błąd API:', { status: response.status, apiResponse: apiErrorData });
          setError(errorMessage);
          return;
        }

        const result: ApiResponse<EventData> = await response.json();

        if (result.data) {
          const fetchedEvent: EventData = result.data;
          if (loggedInUser.id !== fetchedEvent.userId) {
            alert('Błąd: Nie masz uprawnień do edycji tego wydarzenia.');
            navigate(`/events/${eventId}`);
            return;
          }

          setEvent(fetchedEvent);
        } else {
          setError(errorMessage);
        }
      } catch (err) {
        console.error('Błąd sieciowy / krytyczny aplikacji:', err);
        setError(errorMessage);
      } finally {
        setIsLoading(false);
      }
    };

    fetchEventToEdit().catch((err: unknown): void => {
      console.error(`Nieobsłużony błąd w fetchEventToEdit: ${err}`);
    });

  }, [eventId, navigate]);

  if (isLoading) {
    return (<p>Ładowanie wydarzenia do edycji...</p>);
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
      <EventForm event={event}/>
    </div>
  )
}
