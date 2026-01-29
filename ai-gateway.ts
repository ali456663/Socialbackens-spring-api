import { streamText } from '@ai-sdk/';
import 'dotenv/config';
import { openai } from '@ai-sdk/openai';

// En enkel funktion för att prata med AI
export async function chatWithAI(prompt: string) {
    try {
        const result = streamText({
            model: openai('gpt-4'), // Använder GPT-4
            system: 'You are a helpful assistant. Respond in Swedish.',
            prompt: prompt,
        });

        let fullResponse = '';

        // Samla all text från AI:n
        for await (const textPart of result.textStream) {
            fullResponse += textPart;
            let process;
            process.stdout.write(textPart); // Visa i terminal
        }

        return fullResponse;

    } catch (error) {
        console.error('Error talking to AI:', error);
        return 'Sorry, I could not process your request.';
    }
}

// Testfunktion om vi kör filen direkt
let require;
let module;
if (require.main === module) {
    const testPrompt = 'Hej! Kan du förklara AI för en nybörjare?';
    console.log('🧠 Asking AI:', testPrompt);
    console.log('🤖 AI Response:');

    chatWithAI(testPrompt).then(response => {
        console.log('\n\n✅ Conversation complete!');
    });
}