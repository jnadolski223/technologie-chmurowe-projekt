import EventCard from './EventCard.tsx';
import type { EventData } from '../interfaces';

interface EventListProps {
  events: EventData[];
}

export default function EventList({ events }: EventListProps) {
  if (events.length === 0) {
    return (<p>Brak nadchodzących wydarzeń.</p>);
  }

  return (
    <div>
      {events.map((event: EventData) => (
        <EventCard key={event.id} event={event}/>
      ))}
    </div>
  )
}