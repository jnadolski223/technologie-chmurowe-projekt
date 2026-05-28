import type { EventData } from '../interfaces/EventData.ts';
import { Link } from 'react-router-dom';

interface EventCardProps {
  event: EventData
}

function truncateText(text: string, maxLength: number): string {
  if (text.length <= maxLength) {
    return text;
  }

  return text.slice(0, maxLength) + '...';
}

export default function EventCard({ event }: EventCardProps) {
  const shortDescription: string = truncateText(event.description, 100);

  return (
    <div>
      <p><strong>{event.title}</strong></p>
      <p>{event.date}, {event.time}</p>
      <p>{event.location}</p>
      <p>{shortDescription}</p>
      <Link to={`/events/${event.id}`}>Wyświetl</Link>
    </div>
  )
}
