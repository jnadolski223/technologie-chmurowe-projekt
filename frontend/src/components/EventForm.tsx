import { type SubmitEvent, useState } from 'react';
import { type NavigateFunction, useNavigate } from 'react-router-dom';
import { config } from '../config/config.ts';
import type { EventData, EventRequest, UserStorageData } from '../interfaces';

interface EventFormProps {
  event: EventData | null;
}

export default function EventForm({ event }: EventFormProps) {
  const navigate: NavigateFunction = useNavigate();
  const isEditMode: boolean = event != null;

  const [title, setTitle] = useState<string>(event?.title ?? '');
  const [location, setLocation] = useState<string>(event?.location ?? '');
  const [date, setDate] = useState<string>(event?.date ?? '');
  const [time, setTime] = useState<string>(event?.time ?? '');
  const [description, setDescription] = useState<string>(event?.description ?? '');

  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

  const handleSubmit = async (e: SubmitEvent): Promise<void> => {
    e.preventDefault();

    const userDataRaw: string | null = localStorage.getItem(config.userStorageKey);
    if (!userDataRaw) {
      alert('Błąd: Musisz być zalogowany, aby wykonać tę operację.');
      navigate('/login');
      return;
    }

    const loggedInUser: UserStorageData = JSON.parse(userDataRaw);
    const userId: string = loggedInUser.id;

    const eventRequestData: EventRequest = { userId, title, location, date, time, description };

    const url: string = isEditMode
      ? `${config.apiBaseUrl}/events/${event?.id}`
      : `${config.apiBaseUrl}/events`;

    const method: string = isEditMode ? 'PUT' : 'POST';

    const errorMessage: string = `Błąd: Nie udało się ${isEditMode ? 'zaktualizować' : 'utworzyć'} wydarzenia.`;

    try {
      setIsSubmitting(true);

      const response: Response = await fetch(url, {
        method: method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(eventRequestData)
      });

      if (response.ok) {
        alert(isEditMode ? 'Wydarzenie zostało pomyślnie zaktualizowane!' : 'Wydarzenie zostało utworzone!');
        navigate('/events');
      } else {
        alert(errorMessage);
      }
    } catch (err) {
      console.error('Błąd sieciowy / krytyczny aplikacji:', err);
      alert(errorMessage);
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      <h2>{isEditMode ? 'Edytuj wydarzenie' : 'Utwórz nowe wydarzenie'}</h2>

      <div>
        <label htmlFor="title">Tytuł wydarzenia:</label>
        <input
          id="title"
          type="text"
          value={title}
          onChange={(e): void => setTitle(e.target.value)}
          required
          minLength={3}
          maxLength={200}
        />
      </div>

      <div>
        <label htmlFor="location">Lokalizacja wydarzenia:</label>
        <input
          id="location"
          type="text"
          value={location}
          onChange={(e): void => setLocation(e.target.value)}
          required
          minLength={3}
          maxLength={200}
        />
      </div>

      <div>
        <label htmlFor="date">Data wydarzenia:</label>
        <input
          id="date"
          type="date"
          value={date}
          onChange={(e): void => setDate(e.target.value)}
          required
        />
      </div>

      <div>
        <label htmlFor="time">Czas wydarzenia:</label>
        <input
          id="time"
          type="time"
          step={1}
          value={time}
          onChange={(e): void => setTime(e.target.value)}
          required
        />
      </div>

      <div>
        <label htmlFor="description">Opis wydarzenia:</label>
        <textarea
          id="description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          rows={5}
        />
      </div>

      <div>
        <button type="submit" disabled={isSubmitting}>
          {isSubmitting ? 'Zapisywanie...' : isEditMode ? 'Zapisz zmiany' : 'Utwórz wydarzenie'}
        </button>
        <button type="button" onClick={() => navigate(-1)}>
          Anuluj
        </button>
      </div>
    </form>
  );
}