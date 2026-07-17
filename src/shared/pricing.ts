import type { TokenUsage } from './types';

// Approximate on-demand pricing for the hosted model (Claude Sonnet 4.5),
// in USD per million tokens. Used only to give learners a feel for relative
// cost — not a billing system. If the hosted model changes, update these.
export const PRICE_PER_MILLION_TOKENS = {
  input: 3.0,
  output: 15.0,
};

export function costUsd(usage: TokenUsage): number {
  return (
    (usage.inputTokens * PRICE_PER_MILLION_TOKENS.input +
      usage.outputTokens * PRICE_PER_MILLION_TOKENS.output) /
    1_000_000
  );
}

export function formatCost(usd: number): string {
  if (usd < 0.01) return `$${usd.toFixed(4)}`;
  return `$${usd.toFixed(2)}`;
}

export function formatTokens(n: number): string {
  return n.toLocaleString('en-GB');
}
