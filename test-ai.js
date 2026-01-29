// Ladda miljövariabler
require('dotenv').config();

// Enkel test av OpenAI
async function testOpenAI() {
    console.log('🔑 Testing API Key...');
    console.log('Key exists:', !!process.env.OPENAI_API_KEY);
    console.log('Key starts with:', process.env.OPENAI_API_KEY?.substring(0, 10) + '...');

    // Testa om vi kan nå OpenAI
    const response = await fetch('https://api.openai.com/v1/models', {
        headers: {
            'Authorization': `Bearer ${process.env.OPENAI_API_KEY}`
        }
    });

    if (response.ok) {
        console.log('✅ API Key is valid!');
        const data = await response.json();
        console.log(`📊 Available models: ${data.data.length}`);
    } else {
        console.log('❌ API Key is invalid or there is an error');
        console.log('Status:', response.status);
    }
}

// Kör testet
testOpenAI().catch(console.error);