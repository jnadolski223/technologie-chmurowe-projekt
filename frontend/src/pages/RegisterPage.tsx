import { type SubmitEvent, useState } from 'react';
import { type NavigateFunction, useNavigate } from 'react-router-dom';
import { config } from '../config/config.ts';
import type { UserRequest } from '../interfaces';

export default function RegisterPage() {
  const navigate: NavigateFunction = useNavigate();

  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');

  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

  const handleSubmit = async (e: SubmitEvent) => {
    e.preventDefault();

    const registerRequest: UserRequest = { email, password };

    const errorMessage: string = 'Błąd: Nie udało się zalogować - błąd po stronie serwera.';

    try {
      setIsSubmitting(true);

      const response: Response = await fetch(`${config.apiBaseUrl}/users/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(registerRequest)
      });

      if (response.ok) {
        alert('Konto zostało pomyślnie utworzone! Możesz się teraz zalogować');
        navigate('/login');
      } else {
        if (response.status === 409) {
          alert('Błąd: Wybrany adres email jest już zajęty przez innego użytkownika');
        }
      }
    } catch (err) {
      console.error('Błąd sieciowy / krytyczny aplikacji:', err);
      alert(errorMessage);
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <div>
      <h2>Zarejestruj się</h2>

      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="email">E-mail:</label>
          <input
            id="email"
            type="text"
            value={email}
            onChange={(e): void => setEmail(e.target.value)}
            required
          />
        </div>

        <div>
          <label htmlFor="password">Hasło:</label>
          <input
            id="password"
            type="password"
            value={password}
            onChange={(e): void => setPassword(e.target.value)}
            required
            minLength={8}
          />
        </div>

        <div>
          <button type="submit" disabled={isSubmitting}>
            {isSubmitting ? 'Tworzenie konta...' : 'Zarejestruj się'}
          </button>
          <button type="button" onClick={() => navigate('/login')}>
            Msz już konto? Zaloguj się!
          </button>
        </div>
      </form>
    </div>
  )
}
