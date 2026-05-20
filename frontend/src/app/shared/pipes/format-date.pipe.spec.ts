import { FormatDatePipe } from './format-date.pipe';

describe('FormatDatePipe', () => {
  let pipe: FormatDatePipe;

  beforeEach(() => { pipe = new FormatDatePipe(); });

  it('returns — for null', () => {
    expect(pipe.transform(null)).toBe('—');
  });

  it('returns — for undefined', () => {
    expect(pipe.transform(undefined)).toBe('—');
  });

  it('returns — for empty string', () => {
    expect(pipe.transform('')).toBe('—');
  });

  it('returns the original value for an invalid date string', () => {
    expect(pipe.transform('not-a-date')).toBe('not-a-date');
  });

  it('formats a valid ISO date to pt-BR locale', () => {
    const result = pipe.transform('2024-06-15T10:30:00');
    expect(result).toContain('15/06/2024');
    expect(result).toContain('10:30');
  });
});
