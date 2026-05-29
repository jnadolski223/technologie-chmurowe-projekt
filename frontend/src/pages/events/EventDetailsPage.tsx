import { useEffect, useState } from 'react';
import { useParams, useNavigate, type NavigateFunction } from 'react-router-dom';
import EventDetails from '../../components/EventDetails.tsx';
import { config } from '../../config/config.ts';
import type { ApiResponse, BookingData, EventData, UserStorageData } from '../../interfaces';

export default function EventDetailsPage() {
  const { eventId } = useParams<{ eventId: string }>();
  const navigate: NavigateFunction = useNavigate();

  const [event, setEvent] = useState<EventData | null>(null);
  const [bookingCount, setBookingCount] = useState<number>(0);
  const [isOwner, setIsOwner] = useState<boolean>(false);
  const [bookingId, setBookingId] = useState<string | null>(null);

  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect((): void => {
    const fetchEventData = async (): Promise<void> => {
      const errorMessage: string = 'Błąd: Nie udało się pobrać kompletnych danych wydarzenia.';

      try {
        setIsLoading(true);
        setError(null);

        const userDataRaw: string | null = localStorage.getItem(config.userStorageKey);
        const loggedInUser: UserStorageData | null = userDataRaw ? JSON.parse(userDataRaw) : null;

        const [eventResponse, bookingsResponse] = await Promise.all([
          fetch(`${config.apiBaseUrl}/events/${eventId}`).then((res: Response): Promise<ApiResponse<EventData>> => res.json() as Promise<ApiResponse<EventData>>),
          fetch(`${config.apiBaseUrl}/events/${eventId}/bookings`).then((res): Promise<ApiResponse<BookingData[]>> => res.json() as Promise<ApiResponse<BookingData[]>>)
        ]);

        if (eventResponse.data && bookingsResponse.data) {
          const eventData: EventData = eventResponse.data;
          const bookingsList: BookingData[] = bookingsResponse.data;

          setEvent(eventData);
          setBookingCount(bookingsList.length);

          if (loggedInUser) {
            setIsOwner(loggedInUser.id === eventData.userId);

            const userBooking: BookingData | undefined = bookingsList.find((booking: BookingData): boolean => booking.userId === loggedInUser.id);
            setBookingId(userBooking ? userBooking.id : null);
          } else {
            setIsOwner(false);
            setBookingId(null);
          }
        } else {
          setError(errorMessage);
        }
      } catch (err) {
        console.error('Błąd sieciowy / krytyczny aplikacji:', err);
        setError(errorMessage);
      } finally {
        setIsLoading(false);
      }
    }

    fetchEventData().catch((err: unknown): void => {
      console.error(`Nieobsłużony błąd w fetchEventData: ${err}`);
    });
  }, [eventId])


  if (isLoading) {
    return (<div>Trwa ładowanie szczegółów wydarzenia...</div>);
  }

  if (error) {
    return (
      <div>
        <h3>Wystąpił problem</h3>
        <p>{error}</p>
      </div>
    )
  }

  if (!event) {
    navigate('/not-found');
    return;
  }

  return (
    <EventDetails
      event={event}
      bookingCount={bookingCount}
      isOwner={isOwner}
      bookingId={bookingId}
    />
  )
}
