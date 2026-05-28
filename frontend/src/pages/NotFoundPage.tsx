import { Link } from 'react-router-dom';

export default function NotFoundPage() {
  return (
    <div>
      <h1>404 - Strona nie istnieje</h1>
      <p>Ups! Wygląda na to, że zabłądziłeś.</p>
      <Link to="/">Wróć do strony głównej</Link>
    </div>
  )
}
