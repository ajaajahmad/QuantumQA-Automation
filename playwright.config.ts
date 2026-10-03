import { devices, expect } from '@playwright/test';
import { trace } from 'node:console';

const config = {
  testDir: './tests',
  timeout: 30 * 1000,
  expect: {
    timeout: 5000
  },
  reporter: 'html',
  use: {
    browserName: 'chromium',
    headless: false,
    permissions: [],
    trace: 'retain-on-failure',
    screenshot: 'only-on-failure',
    video: 'retain-on-failure',
    launchOptions: {
      args: ['--start-maximized'],
    },
    viewport: null,
  }
};

export default config;