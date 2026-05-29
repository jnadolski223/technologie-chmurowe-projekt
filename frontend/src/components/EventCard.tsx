import { type NavigateFunction, useNavigate } from 'react-router-dom';
import type { EventData } from '../interfaces';

interface EventCardProps {
  event: EventData
}

const truncateText = (text: string, maxLength: number): string => {
  if (text.length <= maxLength) {
    return text;
  }

  return text.slice(0, maxLength) + '...';
}

export default function EventCard({ event }: EventCardProps) {
  const navigate: NavigateFunction = useNavigate();

  const shortDescription: string = truncateText(event.description, 100);

  const handleShowDetails = (): void => {
    navigate(`/events/${event.id}`);
  }

  return (
    <div>
      <p><strong>{event.title}</strong></p>
      <p>{event.date}, {event.time}</p>
      <p>{event.location}</p>
      <p>{shortDescription}</p>
      <button onClick={handleShowDetails}>Wyświetl szczegóły</button>
    </div>
  )
}
