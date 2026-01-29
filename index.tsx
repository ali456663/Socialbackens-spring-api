import { useState } from 'react';

export default function Home() {
    const [messages, setMessages] = useState<{text: string, sender: 'user' | 'ai'}[]>([]);
    const [input, setInput] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const sendMessage = async () => {
        if (!input.trim()) return;

        // Lägg till användarens meddelande
        const userMessage = { text: input, sender: 'user' as const };
        setMessages(prev => [...prev, userMessage]);
        setInput('');
        setIsLoading(true);

        try {
            // Skicka till vår AI API
            const response = await fetch('/api/chat', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ message: input }),
            });

            const data = await response.json();

            // Lägg till AI:s svar
            const aiMessage = { text: data.response, sender: 'ai' as const };
            setMessages(prev => [...prev, aiMessage]);

        } catch (error) {
            console.error('Error:', error);
            const errorMessage = { text: 'Sorry, something went wrong!', sender: 'ai' as const };
            setMessages(prev => [...prev, errorMessage]);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div style={styles.container}>
            <h1 style={styles.title}>🤖 AI Chat Assistant</h1>
            <p style={styles.subtitle}>Ask me anything in Swedish!</p>

            <div style={styles.chatContainer}>
                {messages.length === 0 ? (
                    <div style={styles.welcomeMessage}>
                        <p>👋 Hello! I'm your AI assistant.</p>
                        <p>Try asking me something like:</p>
                        <ul style={styles.exampleList}>
                            <li>"Explain AI to a beginner"</li>
                            <li>"Help me with programming"</li>
                            <li>"Tell me a fun fact"</li>
                        </ul>
                    </div>
                ) : (
                    <div style={styles.messages}>
                        {messages.map((msg, index) => (
                            <div
                                key={index}
                                style={{
                                    ...styles.message,
                                    ...(msg.sender === 'user' ? styles.userMessage : styles.aiMessage)
                                }}
                            >
                                <strong>{msg.sender === 'user' ? 'You:' : 'AI:'}</strong> {msg.text}
                            </div>
                        ))}
                        {isLoading && (
                            <div style={styles.thinking}>AI is thinking...</div>
                        )}
                    </div>
                )}

                <div style={styles.inputContainer}>
                    <input
                        type="text"
                        value={input}
                        onChange={(e) => setInput(e.target.value)}
                        onKeyPress={(e) => e.key === 'Enter' && sendMessage()}
                        placeholder="Type your message here..."
                        style={styles.input}
                        disabled={isLoading}
                    />
                    <button
                        onClick={sendMessage}
                        style={styles.button}
                        disabled={isLoading || !input.trim()}
                    >
                        {isLoading ? 'Sending...' : 'Send'}
                    </button>
                </div>
            </div>

            <div style={styles.footer}>
                <p>Built with Vercel AI SDK & Next.js</p>
                <p style={styles.warning}>⚠️ Your API key is only used locally</p>
            </div>
        </div>
    );
}

// Enkel styling direkt i koden
const styles = {
    container: {
        fontFamily: 'Arial, sans-serif',
        maxWidth: '800px',
        margin: '0 auto',
        padding: '20px',
    },
    title: {
        color: '#333',
        textAlign: 'center' as const,
    },
    subtitle: {
        color: '#666',
        textAlign: 'center' as const,
        marginBottom: '40px',
    },
    chatContainer: {
        background: '#f5f5f5',
        borderRadius: '10px',
        padding: '20px',
        minHeight: '400px',
        display: 'flex',
        flexDirection: 'column' as const,
    },
    welcomeMessage: {
        textAlign: 'center' as const,
        color: '#666',
        flex: 1,
        display: 'flex',
        flexDirection: 'column' as const,
        justifyContent: 'center',
        alignItems: 'center',
    },
    exampleList: {
        textAlign: 'left' as const,
        paddingLeft: '20px',
    },
    messages: {
        flex: 1,
        overflowY: 'auto' as const,
        marginBottom: '20px',
    },
    message: {
        padding: '10px',
        margin: '10px 0',
        borderRadius: '5px',
        maxWidth: '80%',
    },
    userMessage: {
        background: '#0070f3',
        color: 'white',
        alignSelf: 'flex-end',
        marginLeft: 'auto',
    },
    aiMessage: {
        background: '#e0e0e0',
        color: '#333',
    },
    thinking: {
        color: '#666',
        fontStyle: 'italic',
    },
    inputContainer: {
        display: 'flex',
        gap: '10px',
    },
    input: {
        flex: 1,
        padding: '10px',
        border: '1px solid #ddd',
        borderRadius: '5px',
        fontSize: '16px',
    },
    button: {
        padding: '10px 20px',
        background: '#0070f3',
        color: 'white',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
        fontSize: '16px',
    },
    footer: {
        marginTop: '40px',
        textAlign: 'center' as const,
        color: '#999',
        fontSize: '14px',
    },
    warning: {
        color: '#ff9800',
    }
} as const;